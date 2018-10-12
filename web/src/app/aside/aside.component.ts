import {Component, OnInit, ViewEncapsulation} from '@angular/core';
import {ApiServices} from '../services/api-services.service';
import {DatabaseObj} from '../services/pojo/DatabaseListObj';

@Component({
    selector: 'app-aside',
    templateUrl: './aside.component.html',
    styleUrls: ['./aside.component.css'],
    encapsulation: ViewEncapsulation.None,
    providers: [ApiServices]
})
export class AsideComponent implements OnInit {

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
