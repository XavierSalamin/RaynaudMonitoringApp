import { NgModule }      from '@angular/core';
import { CommonModule }  from '@angular/common';
import { FormsModule } from '@angular/forms';

import { CrisisComponent } from './crisis.component';
import { routing } from './crisis.routing';



import { Ng2SmartTableModule } from 'ng2-smart-table';

import { ThemeModule } from '../../@theme/theme.module';
import { SmartTableService } from '../../@core/data/smart-table.service';

@NgModule({
	imports: [
		CommonModule,
		FormsModule,
		routing,
		ThemeModule,
		Ng2SmartTableModule,
	],
	declarations: [
		CrisisComponent
	],
	providers: [
		SmartTableService,
	],
})
export class CrisisModule {}

