import {Component, OnInit} from '@angular/core';
import {ApiServices} from '../services/api-services.service';
import {DatabaseObj} from '../services/pojo/DatabaseListObj';
import {NzMenuItemDirective} from 'ng-zorro-antd';

@Component({
  selector: 'app-sider',
  templateUrl: './sider.component.html',
  styleUrls: ['./sider.component.css'],
  providers: [ApiServices]
})
export class SiderComponent implements OnInit {

  databaseList: DatabaseObj[];


  constructor(private apiServices: ApiServices) {
  }

  getDatabaseList() {
    this.apiServices.getDatabase().subscribe(databaseListObj => {
      if (databaseListObj.success) {
        this.databaseList = databaseListObj.obj;
        console.log(this.databaseList);
      }
    });
  }

  ngOnInit() {
    this.getDatabaseList();
  }
}
