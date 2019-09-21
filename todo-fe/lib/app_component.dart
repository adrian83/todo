import 'package:angular/angular.dart';
import 'package:angular_router/angular_router.dart';
// import 'package:todo_fe/src/auth_service.dart';

import 'src/routes.dart';
import 'src/route_paths.dart';

import 'src/navigation_component.dart';
import 'src/menu_component.dart';
import 'src/auth_service.dart';
import 'src/todo_service.dart';
import 'src/store.dart';

@Component(
  selector: 'my-app',
  template: '''

        <menu></menu>
        
        <div class="container text-center">
          <br/>

          <router-outlet [routes]="Routes.all"></router-outlet>
        
          <footer class="pt-4 my-md-5 pt-md-5 border-top">
            <div class="row">
              <div class="col-12 col-md">
                <small class="d-block mb-3 text-muted">&copy; 2017-2019</small>
              </div>
            </div>
          </footer>
        </div>
        ''',
  directives: [routerDirectives, NavigationComponent, MenuComponent],
  providers: [
    ClassProvider(AuthService), 
    ClassProvider(TodoService), 
    ClassProvider(Store)],
  exports: [RoutePaths, Routes], 
)
class AppComponent {
  final title = 'Todos';
}
