import 'dart:async';

import 'package:angular/angular.dart';
import 'package:angular_router/angular_router.dart';

import 'route_paths.dart';
import 'routes.dart';


@Component(
  selector: 'navigation',
  templateUrl: 'navigation_component.html', 
  directives: [coreDirectives, routerDirectives],
  exports: [RoutePaths, Routes],
  pipes: [commonPipes],
)
class NavigationComponent implements OnInit {

  final Router _router;


  NavigationComponent(this._router);

  void ngOnInit(){

  }

}


