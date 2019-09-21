import 'package:angular/angular.dart';
import 'package:angular_router/angular_router.dart';
import 'package:todo_fe/app_component.template.dart' as ng;

import 'package:http/http.dart';

import 'package:http/browser_client.dart';

import 'main.template.dart' as self;

@GenerateInjector([
  routerProvidersHash, // You can use routerProviders in production
  //ClassProvider(Client, useClass: InMemoryDataService),
  // Using a real back end?
  // Import 'package:http/browser_client.dart' and change the above to:
     ClassProvider(Client, useClass: BrowserClient),
])
final InjectorFactory injector = self.injector$Injector;

void main() {
  runApp(ng.AppComponentNgFactory, createInjector: injector);
}
