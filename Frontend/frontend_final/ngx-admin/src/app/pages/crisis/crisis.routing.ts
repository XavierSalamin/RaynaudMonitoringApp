import { Routes, RouterModule } from '@angular/router';

import { CrisisComponent } from './crisis.component';

const routes: Routes = [
  {
    path: '',
    component: CrisisComponent
  }
];

export const routing = RouterModule.forChild(routes);