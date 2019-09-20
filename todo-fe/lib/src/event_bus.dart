import 'dart:async';


class EventBus {

    final StreamController<bool> _onLoginEvent = new StreamController<bool>();
    Stream<bool> onLoginStream = null;

    static final EventBus _singleton = new EventBus._internal(); 

    factory EventBus() {
      return _singleton;
    }

    EventBus._internal() {
      onLoginStream = _onLoginEvent.stream;
    }

    onEvent(bool signedIn) {
      _onLoginEvent.add(signedIn);
    }
}