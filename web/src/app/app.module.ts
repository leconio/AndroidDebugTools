import {BrowserModule} from '@angular/platform-browser';
import {NgModule} from '@angular/core';

import {AppComponent} from './app.component';
import {ElModule} from 'element-angular';
import {BrowserAnimationsModule} from '@angular/platform-browser/animations';
import {HeaderComponent} from './header/header.component';
import {AsideComponent} from './aside/aside.component';
import {MainComponent} from './main/main.component';
import {RouterModule, Routes} from '@angular/router';
import {DatabaseItemComponent} from './database-item/database-item.component';
import {DatabaseMainComponent} from './database-main/database-main.component';
import {PaginationComponent} from './pagination/pagination.component';

const routes: Routes = [
  {path: 'app', component: AppComponent}
];

@NgModule({
  declarations: [
    AppComponent,
    HeaderComponent,
    AsideComponent,
    MainComponent,
    DatabaseItemComponent,
    DatabaseMainComponent,
    PaginationComponent
  ],
  imports: [
    BrowserModule,
    BrowserAnimationsModule,
    ElModule.forRoot(),
    RouterModule.forRoot(routes)

  ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule {
}
