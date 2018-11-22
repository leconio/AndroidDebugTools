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
import {DatabaseSiderComponent} from './database/database-sider/database-sider.component';
import {HttpErrorHandler} from './http-error-handler.service';
import {MessageService} from './services/message.service';
import {SiderItemMenuComponent} from './database/sider-item-menu/sider-item-menu.component';
import {RouterModule, Routes} from '@angular/router';
import {DatabaseContentComponent} from './database/database-content/database-content.component';
import {DatabaseMainComponent} from './database/database-main/database-main.component';
import {DiskMainComponent} from './disk/disk-main/disk-main.component';
import {DiskSiderComponent} from './disk/disk-sider/disk-sider.component';
import {DiskContentComponent} from './disk/disk-content/disk-content.component';
import { TableTextFormatComponent } from './database/table-text-fomart/table-text-format.component';
import {ClipboardModule} from 'ngx-clipboard';

registerLocaleData(zh);

const databaseRoutes: Routes = [
  {path: ':dbName/:tableName/:dbPath', component: DatabaseContentComponent}
];

const diskRoutes: Routes = [
  {path: ':type', component: DiskContentComponent},
  {path: '', redirectTo: 'inner', pathMatch: 'full'}
];


const routes: Routes = [
  {path: '', redirectTo: 'database', pathMatch: 'full'},
  {path: 'database', component: DatabaseMainComponent, children: databaseRoutes},
  {path: 'storage', component: DiskMainComponent, children: diskRoutes},
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
    DiskSiderComponent,
    DiskContentComponent,
    TableTextFormatComponent,
  ],
  imports: [
    BrowserModule,
    BrowserAnimationsModule,
    FormsModule,
    HttpClientModule,
    NgZorroAntdModule,
    RouterModule.forRoot(routes),
    ClipboardModule
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
