import 'dart:async';

import 'package:angular/angular.dart';
import 'package:angular_router/angular_router.dart';

import 'route_paths.dart';
import 'routes.dart';

import 'auth_service.dart';
import 'todo_service.dart';
import 'todo.dart';
import 'store.dart';

@Component(
  selector: 'todo-list',
  templateUrl: 'todo_list_component.html', 
  directives: [coreDirectives, routerDirectives],
  exports: [RoutePaths, Routes],
  providers: [ClassProvider(Store), ClassProvider(TodoService)],
  pipes: [commonPipes],
)
class TodoListComponent implements OnInit {

  final Router _router;
  Store _store;
  TodoService _todoService;

  List<Todo> todos = [];


  TodoListComponent(this._todoService, this._router, this._store);

  void ngOnInit() async {
    var authToken = _store.getAuthToken();
    //print("auth token: $authToken");
    print("test");
    todos = await _todoService.getAll(authToken);
    print("todos: $todos");
  }

  void create(String text) async {
    var authToken = _store.getAuthToken();
    var todo = await _todoService.create(authToken, text);
    todos.insert(0, todo);
  }

}


