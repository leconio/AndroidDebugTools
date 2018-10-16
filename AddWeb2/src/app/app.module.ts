import {BrowserModule} from '@angular/platform-browser';
import {NgModule} from '@angular/core';

import {AppComponent} from './app.component';
import {BrowserAnimationsModule} from '@angular/platform-browser/animations';
import {FormsModule} from '@angular/forms';
import {HttpClientModule} from '@angular/common/http';
import {NgZorroAntdModule, NZ_I18N, zh_CN} from 'ng-zorro-antd';
import {registerLocaleData} from '@angular/common';
import zh from '@angular/common/locales/zh';
import {HeaderComponent} from './header/header.component';
import {DatabaseSiderComponent} from './database-sider/database-sider.component';
import {HttpErrorHandler} from './http-error-handler.service';
import {MessageService} from './services/message.service';
import {SiderItemMenuComponent} from './sider-item-menu/sider-item-menu.component';
import {RouterModule, Routes} from '@angular/router';
import {DatabaseContentComponent} from './database-content/database-content.component';
import {DatabaseMainComponent} from './database-main/database-main.component';
import {DiskMainComponent} from './disk-main/disk-main.component';

registerLocaleData(zh);

const databaseRoutes: Routes = [
  {path: ':dbName/:tableName/:dbPath', component: DatabaseContentComponent}
];

const routes: Routes = [
  {path: 'database', component: DatabaseMainComponent, children: databaseRoutes},
  {path: 'disk', component: DiskMainComponent},
];

@NgModule({
  declarations: [
    AppComponent,
    HeaderComponent,

    DatabaseMainComponent,
    DatabaseContentComponent,
    DatabaseSiderComponent,
    SiderItemMenuComponent,

    DiskMainComponent,
  ],
  imports: [
    BrowserModule,
    BrowserAnimationsModule,
    FormsModule,
    HttpClientModule,
    NgZorroAntdModule,
    RouterModule.forRoot(routes),
  ],
  providers: [
    {provide: NZ_I18N, useValue: zh_CN},
    MessageService,
    HttpErrorHandler,
  ],
  bootstrap: [AppComponent]
})
export class AppModule {
}
