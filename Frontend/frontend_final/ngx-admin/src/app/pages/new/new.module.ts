import { NgModule }      from '@angular/core';
import { CommonModule }  from '@angular/common';
import { FormsModule } from '@angular/forms';
import { ThemeModule } from '../../@theme/theme.module';
import { NewComponent } from './new.component';
import { routing } from './new.routing';

@NgModule({
  imports: [
      ThemeModule,
    CommonModule,
    FormsModule,
    routing
  ],
  declarations: [
    NewComponent
  ]
})
export class NewModule {}