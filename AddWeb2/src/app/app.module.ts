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
import {SiderComponent} from './sider/sider.component';
import {MainComponent} from './main/main.component';
import {HttpErrorHandler} from './http-error-handler.service';
import {MessageService} from './services/message.service';
import {SiderItemMenuComponent} from './sider-item-menu/sider-item-menu.component';
import {RouterModule, Routes} from '@angular/router';
import { DatabaseContentComponent } from './database-content/database-content.component';

registerLocaleData(zh);

const routes: Routes = [
  {path: 'database/:dbName/:tableName/:dbPath', component: DatabaseContentComponent}
];

@NgModule({
  declarations: [
    AppComponent,
    HeaderComponent,
    SiderComponent,
    MainComponent,
    SiderItemMenuComponent,
    DatabaseContentComponent
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
