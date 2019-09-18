import 'package:angular/angular.dart';
import 'package:angular_router/angular_router.dart';
// import 'package:todo_fe/src/auth_service.dart';

import 'src/routes.dart';
import 'src/route_paths.dart';

import 'src/navigation_component.dart';
import 'src/auth_service.dart';
import 'src/todo_service.dart';
import 'src/store.dart';

@Component(
  selector: 'my-app',
  template: '''
    <h1>{{title}}</h1>
    <navigation></navigation>
  ''',
  directives: [routerDirectives, NavigationComponent],
  providers: [
    ClassProvider(AuthService), 
    ClassProvider(TodoService), 
    ClassProvider(Store)],
  exports: [RoutePaths, Routes], 
)
class AppComponent {
  final title = 'Todos';
}
