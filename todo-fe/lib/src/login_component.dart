import 'package:angular/angular.dart';
import 'package:angular_router/angular_router.dart';


import 'user.dart';
import 'auth_service.dart';

@Component(
  selector: 'login',
  templateUrl: 'login_component.html',
  directives: [coreDirectives],
    providers: [ClassProvider(AuthService)],
  pipes: [commonPipes],
)
class LoginComponent implements OnInit {

  final Router _router;
  AuthService _authService;

  LoginComponent(this._authService, this._router);

  void ngOnInit(){}

  void login(String email, String password) async {
    print("Email: $email, password: $password");
    var login = Login(email, password);
    await _authService.login(login);
  }

}
