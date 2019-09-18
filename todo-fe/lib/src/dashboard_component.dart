import 'package:angular/angular.dart';


@Component(
  selector: 'dashboard',
  templateUrl: 'dashboard_component.html',
  directives: [coreDirectives],
)
class DashboardComponent implements OnInit {

  DashboardComponent();

  void ngOnInit(){}

}
