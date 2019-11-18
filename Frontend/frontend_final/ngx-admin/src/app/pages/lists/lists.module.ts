import { NgModule }      from '@angular/core';
import { CommonModule }  from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Ng2SmartTableModule } from 'ng2-smart-table';
import { ListsComponent } from './lists.component';
import { routing } from './lists.routing';
import { ThemeModule } from '../../@theme/theme.module';
import { SmartTableService } from '../../@core/data/smart-table.service';

@NgModule({
  imports: [
    CommonModule,
    FormsModule,
    ThemeModule,
    routing,
       Ng2SmartTableModule,
  ],
  declarations: [
    ListsComponent
  ],
    providers: [
    SmartTableService,
  ],
})
export class ListsModule {}

