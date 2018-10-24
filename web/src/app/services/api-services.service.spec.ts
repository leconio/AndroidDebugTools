import {TestBed} from '@angular/core/testing';

import {ApiServices, Urls} from './api-services.service';
import {HttpClientTestingModule, HttpTestingController} from '@angular/common/http/testing';
import {HttpErrorHandler} from '../http-error-handler.service';
import {MessageService} from './message.service';
import {HttpClient} from '@angular/common/http';
import {DatabaseListObj} from './pojo/DatabaseListObj';

describe('ApiServices', () => {

    let httpClient: HttpClient;
    let httpTestingController: HttpTestingController;
    let apiService: ApiServices;

    beforeEach(() => TestBed.configureTestingModule({
        // Import the HttpClient mocking services
        imports: [HttpClientTestingModule],
        // Provide the service-under-test and its dependencies
        providers: [
            ApiServices,
            HttpErrorHandler,
            MessageService
        ]
    }));

    // Inject the http, test controller, and service-under-test
    // as they will be referenced by each test.
    httpClient = TestBed.get(HttpClient);
    httpTestingController = TestBed.get(HttpTestingController);
    apiService = TestBed.get(ApiServices);

    afterEach(() => {
        // After every test, assert that there are no more pending requests.
        httpTestingController.verify();
    });


    it('should be created', () => {
        const service: ApiServices = TestBed.get(ApiServices);
        expect(service).toBeTruthy();
    });
    const databaseListObj: DatabaseListObj = null;
    it('shoule be list database', () => {
        apiService.getDatabase().subscribe(
            database => {
                expect(database).toEqual(databaseListObj, 'nottt');
            },
            fail
        );

        // HeroService should have made one request to GET heroes from expected URL
        const req = httpTestingController.expectOne(Urls.LIST_DATABSE);
        expect(req.request.method).toEqual('GET');

        // Respond with the mock heroes
        req.flush(databaseListObj);
    });
})
;
