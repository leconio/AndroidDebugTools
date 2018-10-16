import {Injectable} from '@angular/core';
import {HttpClient, HttpHeaders, HttpParams} from '@angular/common/http';
import {HandleError, HttpErrorHandler} from '../http-error-handler.service';
import {Observable} from 'rxjs';
import {DatabaseListObj} from './pojo/DatabaseListObj';
import {catchError} from 'rxjs/operators';
import {TableListObj} from './pojo/TableListObj';
import {QueryBeanResp} from './pojo/QueryBeanResp';
import {BaseResponse} from './pojo/BaseResponse';

const httpOptions = {
  headers: new HttpHeaders({
    'Content-Type': 'application/json',
  })
};

export class Urls {
  static baseUrl = 'http://127.0.0.1:8089/';
  static url = Urls.baseUrl + 'db/';
  static LIST_DATABSE = Urls.url + 'listDatabase';
  static LIST_TABLE = Urls.url + 'listTable?dbName=';
  static QUERY = Urls.url + 'query';
  static UPDATE = Urls.url + 'update';
  static DELETE = Urls.url + 'delete';
}

@Injectable({
  providedIn: 'root'
})
export class ApiServices {

  PAGE_SIZE = 15;
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

  delete(dbName: string, tableName: string, condition: string): Observable<BaseResponse> {
    const params = new HttpParams()
      .set('dbName', dbName)
      .set('condition', condition)
      .set('tableName', tableName);
    return this.http.get<BaseResponse>(Urls.DELETE, {params})
      .pipe(
        catchError(this.handleError('delete', new BaseResponse()))
      );
  }
}
