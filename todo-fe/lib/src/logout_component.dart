import 'package:angular/angular.dart';
import 'package:angular_router/angular_router.dart';


import 'user.dart';
import 'store.dart';
import 'event_bus.dart';

@Component(
  selector: 'logout',
  templateUrl: 'logout_component.html',
  directives: [coreDirectives],
    providers: [ClassProvider(Store), ClassProvider(EventBus)],
  pipes: [commonPipes],
)
class LogoutComponent implements OnInit {

  final Router _router;
  Store _store;
  EventBus _eventBus;

  LogoutComponent(this._store, this._router, this._eventBus);

  void ngOnInit(){
    _store.storeAuthToken(null);
    _eventBus.onEvent(false);
  }


}
