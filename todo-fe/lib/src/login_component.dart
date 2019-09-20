import 'package:angular/angular.dart';
import 'package:angular_router/angular_router.dart';


import 'user.dart';
import 'auth_service.dart';
import 'event_bus.dart';
import 'route_paths.dart';

@Component(
  selector: 'login',
  templateUrl: 'login_component.html',
  directives: [coreDirectives],
    providers: [ClassProvider(AuthService), ClassProvider(EventBus)],
  pipes: [commonPipes],
)
class LoginComponent implements OnInit {

  Router _router;
  AuthService _authService;
  EventBus _eventBus;

  LoginComponent(this._authService, this._router, this._eventBus);

  void ngOnInit(){}

  void login(String email, String password) async {
    print("Email: $email, password: $password");
    var login = Login(email, password);
    await _authService.login(login);
    _eventBus.onEvent(true);
    print("go to home, router $_router");
    _router.navigate(RoutePaths.dashboard.toUrl());
    print("go to home, router $_router");
  }

}
