import {Component, Input, OnInit} from '@angular/core';
import {DatabaseObj} from '../../services/pojo/DatabaseListObj';
import {ApiServices} from '../../services/api-services.service';

@Component({
  selector: 'app-sider-item-menu',
  templateUrl: './sider-item-menu.component.html',
  styleUrls: ['./sider-item-menu.component.css'],
  providers: [ApiServices]
})
export class SiderItemMenuComponent implements OnInit {

  @Input()
  database: DatabaseObj;

  tableList: string[] = [];

  constructor(private apiServices: ApiServices) {
  }

  ngOnInit() {
  }

  openChange(isOpen: boolean) {
    if (isOpen) {
      this.apiServices.getTables(this.database.name).subscribe(tables => {
        if (tables.success) {
          this.tableList = tables.obj;
        }
      });
    }
  }
}
