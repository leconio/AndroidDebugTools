import {Component, OnInit} from '@angular/core';
import {ApiServices} from '../services/api-services.service';
import {DatabaseObj} from '../services/pojo/DatabaseListObj';

@Component({
  selector: 'app-database-sider',
  templateUrl: './database-sider.component.html',
  styleUrls: ['./database-sider.component.css'],
  providers: [ApiServices]
})
export class DatabaseSiderComponent implements OnInit {

  databaseList: DatabaseObj[];


  constructor(private apiServices: ApiServices) {
  }

  getDatabaseList() {
    this.apiServices.getDatabase().subscribe(databaseListObj => {
      if (databaseListObj.success) {
        this.databaseList = databaseListObj.obj;
      }
    });
  }

  ngOnInit() {
    this.getDatabaseList();
  }
}
