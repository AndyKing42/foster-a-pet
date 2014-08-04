package org.fosterapet.login;

import java.util.Iterator;
import java.util.List;
import java.util.TreeMap;
import java.util.TreeSet;
import org.fosterapet.client.ClientFactoryBase;
import org.fosterapet.client.IClientEnums.EDTOEventType;
import org.fosterapet.client.IClientEnums.EListDataProviderType;
import org.fosterapet.client.event.LocPersonListLoadedEvent;
import org.fosterapet.client.event.LoginResponseEvent;
import org.fosterapet.client.event.PersonFosterHistoryListLoadedEvent;
import org.fosterapet.client.event.PersonRelationshipListLoadedEvent;
import org.fosterapet.client.event.dtoevent.LocPersonEvent;
import org.fosterapet.client.event.dtoevent.LocPersonEvent.ILocPersonEventHandler;
import org.fosterapet.client.model.dto.IFosterHistoryProxy;
import org.fosterapet.client.model.dto.ILocPersonProxy;
import org.fosterapet.client.model.dto.IPersonProxy;
import org.fosterapet.client.model.dto.IPersonRelationshipProxy;
import org.fosterapet.shared.IRemoteServiceAsync;
import org.fosterapet.shared.requestcontext.IPersonRequestContext;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.view.client.ListDataProvider;
import com.google.web.bindery.requestfactory.shared.Receiver;
import com.google.web.bindery.requestfactory.shared.ServerFailure;

public class PersonCache extends PersonCacheBase implements ILocPersonEventHandler {
//--------------------------------------------------------------------------------------------------
private final TreeMap<Integer, ListDataProvider<IFosterHistoryProxy>>      _fosterHistoryDataProviderByPersonIdMap;
private final TreeMap<Integer, ListDataProvider<ILocPersonProxy>>          _locPersonDataProviderByPersonIdMap;
private int                                                                _loginPersonId;
private final TreeMap<Integer, ListDataProvider<IPersonRelationshipProxy>> _personRelationshipDataProviderByPersonIdMap;
//--------------------------------------------------------------------------------------------------
public PersonCache(final ClientFactoryBase clientFactory) {
  super(clientFactory);
  _fosterHistoryDataProviderByPersonIdMap = new TreeMap<Integer, ListDataProvider<IFosterHistoryProxy>>();
  _locPersonDataProviderByPersonIdMap = new TreeMap<Integer, ListDataProvider<ILocPersonProxy>>();
  _personRelationshipDataProviderByPersonIdMap = new TreeMap<Integer, ListDataProvider<IPersonRelationshipProxy>>();
  _clientFactory.getFAPEventBus().addHandler(LocPersonEvent.LocPersonEventType, this);
} // PersonCacheBase()
//--------------------------------------------------------------------------------------------------
@Override
protected void addToListDataProvider(final EListDataProviderType listDataProviderType,
                                     final List<IPersonProxy> dtoList, final IPersonProxy person) {
  int dtoListIndex;
  for (dtoListIndex = 0; dtoListIndex < dtoList.size() &&
                         person.getDisplayName().compareToIgnoreCase(dtoList.get(dtoListIndex).getDisplayName()) >= 0; ++dtoListIndex) {
    //
  }
  dtoList.add(dtoListIndex, person);
} // addToListDataProvider()
//--------------------------------------------------------------------------------------------------
public ListDataProvider<IFosterHistoryProxy> getFosterHistoryDataProvider(final IPersonProxy person) {
  ListDataProvider<IFosterHistoryProxy> result = _fosterHistoryDataProviderByPersonIdMap.get(person.getPersonId());
  if (result == null) {
    result = new ListDataProvider<IFosterHistoryProxy>(FosterHistoryCacheBase.KeyProvider);
    _fosterHistoryDataProviderByPersonIdMap.put(person.getPersonId(), result);
    loadFosterHistoryList(person);
  }
  return result;
} // getFosterHistoryDataProvider()
//--------------------------------------------------------------------------------------------------
public ListDataProvider<ILocPersonProxy> getLocPersonDataProvider(final IPersonProxy person) {
  ListDataProvider<ILocPersonProxy> result = _locPersonDataProviderByPersonIdMap.get(person.getPersonId());
  if (result == null) {
    result = new ListDataProvider<ILocPersonProxy>(LocPersonCacheBase.KeyProvider);
    _locPersonDataProviderByPersonIdMap.put(person.getPersonId(), result);
    loadLocPersonList(person);
  }
  return result;
} // getLocPersonDataProvider()
//--------------------------------------------------------------------------------------------------
public ListDataProvider<IPersonRelationshipProxy> getPersonRelationshipDataProvider(final IPersonProxy person) {
  ListDataProvider<IPersonRelationshipProxy> result = _personRelationshipDataProviderByPersonIdMap.get(person.getPersonId());
  if (result == null) {
    result = new ListDataProvider<IPersonRelationshipProxy>(PersonRelationshipCacheBase.KeyProvider);
    _personRelationshipDataProviderByPersonIdMap.put(person.getPersonId(), result);
    loadPersonRelationshipList(person);
  }
  return result;
} // getPersonRelationshipDataProvider()
//--------------------------------------------------------------------------------------------------
public void loadFosterHistoryList(final IPersonProxy person) {
  final IPersonRequestContext requestContext = _clientFactory.getRequestFactory().newPersonRequestContext();
  requestContext.loadFosterHistoryList(person.getPersonId()).fire( //
  new Receiver<List<IFosterHistoryProxy>>() {
    @Override
    public void onFailure(final ServerFailure error) {
      Window.alert("Request failed:" + error.getMessage());
      super.onFailure(error);
    } // onFailure()
    @Override
    public void onSuccess(final List<IFosterHistoryProxy> response) {
      final ListDataProvider<IFosterHistoryProxy> dataProvider = getFosterHistoryDataProvider(person);
      dataProvider.setList(response);
      final LocCache locCache = _clientFactory.getCacheManager().getLocCache();
      TreeSet<Integer> locIdSet = null;
      for (final IFosterHistoryProxy fosterHistory : response) {
        final int locId = fosterHistory.getLocId();
        if (locCache.getDTOById(locId) == null) {
          if (locIdSet == null) {
            locIdSet = new TreeSet<Integer>();
          }
          locIdSet.add(locId);
        }
      }
      PersonFosterHistoryListLoadedEvent event = new PersonFosterHistoryListLoadedEvent(person,
                                                                                        true, false);
      if (locIdSet == null) {
        _clientFactory.getFAPEventBus().fireEvent("PersonCache.loadFosterHistoryList(1)", event);
      }
      else {
        locCache.selectUsingDTOIdSet(locIdSet, event);
      }
      final PetCache petCache = _clientFactory.getCacheManager().getPetCache();
      TreeSet<Integer> petIdSet = null;
      for (final IFosterHistoryProxy fosterHistory : response) {
        final int petId = fosterHistory.getPetId();
        if (petCache.getDTOById(petId) == null) {
          if (petIdSet == null) {
            petIdSet = new TreeSet<Integer>();
          }
          petIdSet.add(petId);
        }
      }
      event = new PersonFosterHistoryListLoadedEvent(person, false, true);
      if (petIdSet == null) {
        _clientFactory.getFAPEventBus().fireEvent("PersonCache.loadFosterHistoryList(2)", event);
      }
      else {
        petCache.selectUsingDTOIdSet(petIdSet, event);
      }
    } // onSuccess()
  });
} // loadFosterHistoryList()
//--------------------------------------------------------------------------------------------------
public void loadLocPersonList(final IPersonProxy person) {
  final IPersonRequestContext requestContext = _clientFactory.getRequestFactory().newPersonRequestContext();
  requestContext.loadLocPersonList(person.getPersonId()).fire( //
  new Receiver<List<ILocPersonProxy>>() {
    @Override
    public void onFailure(final ServerFailure error) {
      Window.alert("Request failed:" + error.getMessage());
      super.onFailure(error);
    } // onFailure()
    @Override
    public void onSuccess(final List<ILocPersonProxy> response) {
      final ListDataProvider<ILocPersonProxy> dataProvider = getLocPersonDataProvider(person);
      dataProvider.setList(response);
      final LocCache locCache = _clientFactory.getCacheManager().getLocCache();
      TreeSet<Integer> locIdSet = null;
      for (final ILocPersonProxy locPerson : response) {
        final int locId = locPerson.getLocId();
        if (locCache.getDTOById(locId) == null) {
          if (locIdSet == null) {
            locIdSet = new TreeSet<Integer>();
          }
          locIdSet.add(locId);
        }
      }
      if (locIdSet == null) {
        _clientFactory.getFAPEventBus().fireEvent("PersonCache.loadLocPersonList",
                                                  new LocPersonListLoadedEvent(person));
      }
      else {
        locCache.selectUsingDTOIdSet(locIdSet, new LocPersonListLoadedEvent(person));
      }
    } // onSuccess()
  });
} // loadLocPersonList()
//--------------------------------------------------------------------------------------------------
public void loadPersonRelationshipList(final IPersonProxy person) {
  final IPersonRequestContext requestContext = _clientFactory.getRequestFactory().newPersonRequestContext();
  requestContext.loadPersonRelationshipList(person.getPersonId()).fire( //
  new Receiver<List<IPersonRelationshipProxy>>() {
    @Override
    public void onFailure(final ServerFailure error) {
      Window.alert("Request failed:" + error.getMessage());
      super.onFailure(error);
    } // onFailure()
    @Override
    public void onSuccess(final List<IPersonRelationshipProxy> response) {
      final ListDataProvider<IPersonRelationshipProxy> dataProvider = getPersonRelationshipDataProvider(person);
      dataProvider.setList(response);
      TreeSet<Integer> personIdSet = null;
      for (final IPersonRelationshipProxy personRelationship : response) {
        final int personId = personRelationship.getPersonId2();
        if (getDTOById(personId) == null) {
          if (personIdSet == null) {
            personIdSet = new TreeSet<Integer>();
          }
          personIdSet.add(personId);
        }
      }
      if (personIdSet == null) {
        _clientFactory.getFAPEventBus().fireEvent("PersonCache.loadPersonRelationshipList",
                                                  new PersonRelationshipListLoadedEvent(person));
      }
      else {
        selectUsingDTOIdSet(personIdSet, new PersonRelationshipListLoadedEvent(person));
      }
    } // onSuccess()
  });
} // loadPersonRelationshipList()
//--------------------------------------------------------------------------------------------------
public final void login(final String loginName, final String password) {
  _loginPersonId = 0;
  final IRemoteServiceAsync remoteService = _clientFactory.getRemoteServiceAsync();
  remoteService.login( //
  loginName, password, new AsyncCallback<Integer>() {
    @Override
    public void onFailure(final Throwable caught) {} // onFailure()
    @Override
    public void onSuccess(final Integer personId) {
      _loginPersonId = personId;
      _clientFactory.getFAPEventBus().fireEvent("PersonCache.login",
                                                new LoginResponseEvent(personId));
      _clientFactory.getRequestFactoryResender().resend();
    } // onSuccess()
  });
} // login()
//--------------------------------------------------------------------------------------------------
@Override
public void onLocPersonEvent(final LocPersonEvent locPersonEvent) {
  if (locPersonEvent.getDTOEventType() == EDTOEventType.Saved) {
    final ILocPersonProxy savedLocPerson = locPersonEvent.getDTO();
    final ListDataProvider<ILocPersonProxy> locPersonDataProvider = _locPersonDataProviderByPersonIdMap.get(savedLocPerson.getPersonId());
    if (locPersonDataProvider != null) {
      final List<ILocPersonProxy> locPersonList = locPersonDataProvider.getList();
      int listIndex;
      for (listIndex = 0; listIndex < locPersonList.size(); ++listIndex) {
        if (locPersonList.get(listIndex).getLocPersonId() == savedLocPerson.getLocPersonId()) {
          break;
        }
      }
      if (listIndex < locPersonList.size()) {
        locPersonList.set(listIndex, savedLocPerson);
      }
    }
  }
} // onLocPersonEvent()
//--------------------------------------------------------------------------------------------------
public void reloadLocPerson(final IPersonProxy person) {
  _locPersonDataProviderByPersonIdMap.remove(person.getPersonId());
} // reloadLocPerson()
//--------------------------------------------------------------------------------------------------
public void removeFosterHistories(final IPersonProxy person,
                                  final TreeSet<Integer> fosterHistoryIdSet) {
  final Iterator<IFosterHistoryProxy> iterator = getFosterHistoryDataProvider(person).getList().iterator();
  while (iterator.hasNext()) {
    if (fosterHistoryIdSet.contains(iterator.next().getFosterHistoryId())) {
      iterator.remove();
    }
  }
} // removeFosterHistories
//--------------------------------------------------------------------------------------------------
public void removeLocPersons(final IPersonProxy person, final TreeSet<Integer> locPersonIdSet) {
  final Iterator<ILocPersonProxy> iterator = getLocPersonDataProvider(person).getList().iterator();
  while (iterator.hasNext()) {
    if (locPersonIdSet.contains(iterator.next().getLocPersonId())) {
      iterator.remove();
    }
  }
} // removeLocPersons
//--------------------------------------------------------------------------------------------------
}