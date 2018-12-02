import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { UserRequestService } from './shared/user-request/user-request.service';
import { HttpClientModule } from '@angular/common/http';
import { UserRequestListComponent } from './user-request-list/user-request-list.component';
import { ScripthackComponent } from './scripthack/scripthack.component';

@NgModule({
  declarations: [
    AppComponent,
    UserRequestListComponent,
    ScripthackComponent
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    HttpClientModule
  ],
  providers: [UserRequestService],
  bootstrap: [AppComponent]
})
export class AppModule { }
