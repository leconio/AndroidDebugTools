import {Injectable} from '@angular/core';
import {HttpClient, HttpHeaders, HttpParams} from '@angular/common/http';
import {HandleError, HttpErrorHandler} from '../http-error-handler.service';
import {Observable} from 'rxjs';
import {DatabaseListObj} from './pojo/DatabaseListObj';
import {catchError, map} from 'rxjs/operators';
import {TableListObj} from './pojo/TableListObj';
import {QueryBeanResp} from './pojo/QueryBeanResp';
import {BaseResponse} from './pojo/BaseResponse';
import {FileListObj} from './pojo/FileListObj';
import {VersionObj} from './pojo/VersionObj';
import {environment} from '../../environments/environment';

const httpOptions = {
  headers: new HttpHeaders({
    'Content-Type': 'application/json',
  })
};

export class Urls {
  static baseUrl = environment.production ? 'http://' + window.location.host + '/' : 'http://127.0.0.1:8089/';
  static dbUrl = Urls.baseUrl + 'db/';
  static diskUrl = Urls.baseUrl + 'disk/';
  static LIST_DATABSE = Urls.dbUrl + 'listDatabase';
  static LIST_TABLE = Urls.dbUrl + 'listTable?dbName=';
  static QUERY = Urls.dbUrl + 'query';
  static UPDATE = Urls.dbUrl + 'update';
  static DELETE = Urls.dbUrl + 'delete';
  static VERSION = Urls.dbUrl + 'version';

  static FILE_LIST = Urls.diskUrl + 'list';
}

@Injectable({
  providedIn: 'root'
})
export class ApiServices {

  cache = {
    key: '',
    body: null
  };

  PAGE_SIZE = 10;
  private readonly handleError: HandleError;

  constructor(
    private http: HttpClient,
    private httpErrorHandler: HttpErrorHandler
  ) {
    this.handleError = httpErrorHandler.createHandleError('ApiServices');
  }

  getDatabase(): Observable<DatabaseListObj> {
    return this.http.get<DatabaseListObj>(Urls.LIST_DATABSE)
      .pipe(
        catchError(this.handleError('listdatabse', new DatabaseListObj()))
      );
  }

  getTables(dbName: string): Observable<TableListObj> {
    return this.http.get<TableListObj>(Urls.LIST_TABLE + dbName)
      .pipe(
        catchError(this.handleError('listtable', new TableListObj()))
      );
  }

  query(dbName: string, tableName: string, condition: string = '', limit: number = this.PAGE_SIZE, offset: number = 0)
    : Observable<QueryBeanResp> {
    const params = new HttpParams()
      .set('dbName', dbName)
      .set('tableName', tableName)
      .set('condition', condition)
      .set('limit', String(limit))
      .set('offset', String(offset));
    return this.http.get<QueryBeanResp>(Urls.QUERY, {params: params})
      .pipe(
        catchError(this.handleError('query', new QueryBeanResp()))
      );
  }

  queryByKeyword(dbName: string, tableName: string) {
    let a;
    if (this.cache.key === dbName + tableName) {
      a = Observable.create((subscribe) => {
        subscribe.next(this.cache.body);
        subscribe.complete();
      });
    } else {
      this.cache.key = dbName + tableName;
      a = this.query(dbName, tableName, null, 0, 0)
        .pipe(map(value => this.cache.body = value));
    }
    return a;
  }

  queryByPage(dbName: string, tableName: string, page: number = 0, condition: string = ''): Observable<QueryBeanResp> {
    return this.query(dbName, tableName, condition, this.PAGE_SIZE, this.PAGE_SIZE * page);
  }

  update(dbName: string, tableName: string, body: any): Observable<BaseResponse> {
    const params = new HttpParams()
      .set('dbName', dbName)
      .set('tableName', tableName);
    return this.http.post<BaseResponse>(Urls.UPDATE + '?' + params.toString(), body, httpOptions)
      .pipe(
        catchError(this.handleError('update', new BaseResponse()))
      );
  }

  delete(dbName: string, tableName: string, condition: any): Observable<BaseResponse> {
    const params = new HttpParams()
      .set('dbName', dbName)
      .set('condition', JSON.stringify(condition))
      .set('tableName', tableName);
    return this.http.get<BaseResponse>(Urls.DELETE, {params})
      .pipe(
        catchError(this.handleError('delete', new BaseResponse()))
      );
  }


  getFileList(type: string, path: string) {
    const params = new HttpParams()
      .set('path', path)
      .set('type', type);
    return this.http.get<FileListObj>(Urls.FILE_LIST, {params})
      .pipe(
        catchError(this.handleError('file_list', new FileListObj()))
      );
  }

  downloadFile(isFolder: boolean, path: string, callback: any) {
    // const aPath = path.split('/');
    // const fileName = aPath[aPath.length - 1];
    // this.http.get(Urls.baseUrl + 'file' + path, {
    //   responseType: 'blob'
    // }).subscribe((data) => {
    //   const blob: Blob = new Blob([data], {
    //     type: 'application/octet-stream'
    //   });
    //   const url: string = URL.createObjectURL(blob);
    //   const link: HTMLElement = document.createElement('a');
    //   link.setAttribute('href', url);
    //   if (isFolder) {
    //     link.setAttribute('download', fileName + '.zip');
    //   } else {
    //     link.setAttribute('download', fileName);
    //   }
    //   link.style.visibility = 'hidden';
    //   document.body.appendChild(link);
    //   link.click();
    //   document.body.removeChild(link);
    //   callback();
    // });
    window.open(Urls.baseUrl + 'file' + path);
    callback();
  }

  version(dbName: string) {
    const params = new HttpParams()
      .set('dbName', dbName);
    return this.http.get<VersionObj>(Urls.VERSION, {params}).pipe(
      catchError(this.handleError('file_list', new VersionObj())
      ));
  }
}
