import {Component, Input, OnInit, ViewEncapsulation} from '@angular/core';
import {DatabaseObj} from '../services/pojo/DatabaseListObj';
import {ApiServices} from '../services/api-services.service';
import {ElCollapse} from 'element-angular/release/collapse/collapse';

@Component({
    selector: 'app-database-item',
    templateUrl: './database-item.component.html',
    styleUrls: ['./database-item.component.css'],
    encapsulation: ViewEncapsulation.None,
    providers: [ApiServices]
})
export class DatabaseItemComponent implements OnInit {

    @Input()
    database: DatabaseObj;

    @Input()
    index: number;

    @Input()
    itemChange: ElCollapse;

    tables: string[] = [];

    constructor(private apiServices: ApiServices) {
    }

    ngOnInit() {
        this.itemChange.modelChange.subscribe((which: string[]) => {
            if (Number(which[0].trim()) === this.index) {
                this.apiServices.getTables(this.database.name).subscribe(tables => {
                    if (tables.success) {
                        this.tables = tables.obj;
                        console.log(this.tables);
                    }
                });
            }
        });
    }

}
