import {Component, OnInit} from '@angular/core';
import {ActivatedRoute} from '@angular/router';
import {ApiServices, Urls} from '../../services/api-services.service';
import {async} from 'rxjs/internal/scheduler/async';

@Component({
  selector: 'app-database-content',
  templateUrl: './database-content.component.html',
  styleUrls: ['./database-content.component.css']
})
export class DatabaseContentComponent implements OnInit {

  dbName: string;
  dbPath: string;
  tableName: string;
  version: string;

  /**
   * 服务器必须有个唯一标识字段
   */
  key = 'id';
  dataSet: any[] = [];
  columns: string[] = [];
  pageSize: number = this.apiServices.PAGE_SIZE;
  count: number;
  pageIndex: number;
  editCache = {};
  searchValue: string;

  constructor(private route: ActivatedRoute, private apiServices: ApiServices) {

  }

  ngOnInit() {
    this.route.params.subscribe((params) => {
      this.dbName = params['dbName'];
      this.tableName = params['tableName'];
      this.dbPath = params['dbPath'];
      this.pageIndex = 1;
      this.dataSet = [];
      this.query(0);
      this.getVersion();
    });
  }

  onQuerySearch(keywords: string): void {
    if (keywords) {
      this.apiServices.queryByKeyword(this.dbName, this.tableName)
        .subscribe((result) => {
          if (result.success) {
            this.dataSet = result.obj.list.filter((value) => {
              return JSON.stringify(value).indexOf(keywords) !== -1;
            });
            this.columns = result.obj.columns;
            this.count = this.dataSet.length;
            this.updateEditCache();
          }
        });
    }
  }

  query(page: number) {
    this.apiServices.queryByPage(this.dbName, this.tableName, page)
      .subscribe((result) => {
        if (result.success) {
          this.dataSet = result.obj.list;
          this.columns = result.obj.columns;
          this.count = result.obj.pageInfo.count;
          this.updateEditCache();
        }
      });
  }

  pageIndexChange(pageIndex: number) {
    this.pageIndex = pageIndex;
    this.query(pageIndex - 1);
  }

  downloadClick(dbPath: string) {
    this.apiServices.downloadFile(dbPath, () => {
    });
  }

  updateEditCache(): void {
    this.dataSet.forEach(item => {
      if (!this.editCache[item[this.key]]) {
        this.editCache[item[this.key]] = {
          edit: false,
          data: {...item}
        };
      }
    });
  }

  startEdit(id: string): void {
    this.editCache[id].edit = true;
  }

  cancelEdit(id: string): void {
    this.editCache[id].edit = false;
  }

  saveEdit(id: string): void {
    const index = this.dataSet.findIndex(item => item[this.key] === id);
    Object.assign(this.dataSet[index], this.editCache[id].data);
    this.apiServices.update(this.dbName, this.tableName, this.getUpdateBody(Number(id), this.toCondition(this.dataSet[index])))
      .subscribe((result) => {
        this.editCache[id].edit = false;
      });
  }

  sureDel(id: string) {
    this.editCache[id].del = false;
    this.apiServices.delete(this.dbName, this.tableName, this.getDelCondtion(Number(id)))
      .subscribe((resp) => {
        this.pageIndex = 1;
        this.query(0);
      });
  }

  search(column) {
    console.log(this.searchValue);
    console.log(column);
  }

  /**
   * 把item变成访问数据库的condition，（除了key）
   * TODO :冲突问题
   * @param body item
   */
  private toCondition(body: any): string {
    let cond = '';
    this.columns.forEach((col) => {
      if (col !== this.key) {
        let content;
        if (body[col]) {
          content = body[col];
        } else {
          content = '';
        }
        cond += (col + '靐龘' + content + '驫羴');
      }
    });
    return cond;
  }

  private getDelCondtion(id: number) {
    return this.key + '靐龘' + id;
  }

  private getUpdateBody(id: number, newVal: string) {
    return {
      'condition': this.key + '靐龘' + id,
      'newValue': newVal
    };
  }

  private getVersion() {
    this.apiServices.version(this.dbName)
      .subscribe((result) => {
        if (result.success) {
          this.version = result.obj.version;
        }
      });
  }
}
