import {NgModule} from '@angular/core';
import {CommonModule} from '@angular/common';
import {FormsModule} from '@angular/forms';


import {Ng2SmartTableModule} from 'ng2-smart-table';

import {ThemeModule} from '../../@theme/theme.module';
import {SmartTableService} from '../../@core/data/smart-table.service';
import {routing} from './adminUsers.routing';
import {AdminUsersComponent} from "./adminUsers.component";

@NgModule({
  imports: [
    CommonModule,
    FormsModule,
    routing,
    ThemeModule,
    Ng2SmartTableModule,
  ],
  declarations: [
    AdminUsersComponent,
  ],
  providers: [
    SmartTableService,
  ],
})
export class AdminUsersModule {
}

