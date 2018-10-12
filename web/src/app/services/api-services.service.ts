import {Injectable} from '@angular/core';
import {HttpClient, HttpHeaders} from '@angular/common/http';
import {HandleError, HttpErrorHandler} from '../http-error-handler.service';
import {Observable} from 'rxjs';
import {DatabaseListObj} from './pojo/DatabaseListObj';
import {catchError} from 'rxjs/operators';
import {TableListObj} from './pojo/TableListObj';

const httpOptions = {
    header: new HttpHeaders({
        'Content-Type': 'application/json'
    })
};

export class Urls {
    static url = 'http://127.0.0.1:8089/db/';
    static LIST_DATABSE = Urls.url + 'listDatabase';
    static LIST_TABLE = Urls.url + 'listTable?dbName=';
}

@Injectable({
    providedIn: 'root'
})
export class ApiServices {
    private handleError: HandleError;

    constructor(
        private http: HttpClient,
        httpErrorHandler: HttpErrorHandler
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
                catchError(this.handleError('listdatabse', new TableListObj()))
            );
    }

}
