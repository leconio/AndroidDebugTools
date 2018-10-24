import {Component, OnInit} from '@angular/core';
import {ActivatedRoute} from '@angular/router';
import {ApiServices} from '../../services/api-services.service';
import {subscribeOn} from 'rxjs/operators';
import {async} from 'rxjs/internal/scheduler/async';
import {ColumnsInfo} from '../../services/pojo/QueryBeanResp';

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
  pk = 'id';
  dataSet: any[] = [];
  columns: ColumnsInfo[] = [];
  pageSize: number = this.apiServices.PAGE_SIZE;
  count: number;
  pageIndex: number;
  editCache = {};
  searchValue: string;
  isLoading: boolean;

  constructor(private route: ActivatedRoute, private apiServices: ApiServices) {

  }

  ngOnInit() {
    this.route.params.subscribe((params) => {
      this.dbName = params['dbName'];
      this.tableName = params['tableName'];
      this.dbPath = params['dbPath'];
      this.pageIndex = 1;
      this.dataSet = [];
      this.editCache = {};
      this.query(0);
      this.getVersion();
    });
  }

  onQuerySearch(keywords: string): void {
    this.isLoading = true;
    if (keywords) {
      this.apiServices.queryByKeyword(this.dbName, this.tableName)
        .pipe(subscribeOn(async))
        .subscribe((result) => {
          if (result && result.success) {
            this.dataSet = result.obj.list
              .filter((value) => JSON.stringify(value).indexOf(keywords) !== -1)
              .slice(0, this.apiServices.PAGE_SIZE);
            this.columns = result.obj.columns;
            this.columns.forEach((colInfo) => {
              if (colInfo.isPrimary) {
                this.pk = colInfo.columnName;
              }
            });
            this.count = this.dataSet.length;
            this.updateEditCache();
          }
        });
    } else {
      this.pageIndex = 1;
      this.dataSet = [];
      this.query(0);
    }
  }

  query(page: number) {
    this.isLoading = true;
    this.apiServices.queryByPage(this.dbName, this.tableName, page)
      .subscribe((result) => {
        if (result.success) {
          this.dataSet = result.obj.list;
          this.columns = result.obj.columns;
          this.columns.forEach((colInfo) => {
            if (colInfo.isPrimary) {
              this.pk = colInfo.columnName;
            }
          });
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
    this.apiServices.downloadFile(false, dbPath, () => {
    });
  }

  updateEditCache(): void {
    this.isLoading = false;
    this.dataSet.forEach(item => {
      if (!this.editCache[item[this.pk]]) {
        this.editCache[item[this.pk]] = {
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
    const index = this.dataSet.findIndex(item => item[this.pk] === id);
    Object.assign(this.dataSet[index], this.editCache[id].data);
    this.apiServices.update(this.dbName, this.tableName, this.getUpdateBody(id, this.toCondition(this.dataSet[index])))
      .subscribe((result) => {
        this.editCache[id].edit = false;
      });
  }

  sureDel(id: string) {
    this.editCache[id].del = false;
    this.apiServices.delete(this.dbName, this.tableName, this.getDelCondition(id))
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
   * @param body item
   */
  private toCondition(body: any): string {
    let cond = '';
    this.columns.forEach((col) => {
      if (!col.isPrimary) {
        let content;
        if (body[col.columnName]) {
          content = body[col.columnName];
        } else {
          content = '';
        }
        cond += (col.columnName + '靐龘' + content + '驫羴');
      }
    });
    return cond;
  }

  private getDelCondition(pk: string) {
    return this.pk + '靐龘' + pk;
  }

  private getUpdateBody(pk: string, newVal: string) {
    return {
      'condition': this.pk + '靐龘' + pk,
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
