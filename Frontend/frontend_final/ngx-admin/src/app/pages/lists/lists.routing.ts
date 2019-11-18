import { Routes, RouterModule } from '@angular/router';

import { ListsComponent } from './lists.component';

const routes: Routes = [
  {
    path: '',
    component: ListsComponent
  }
];

export const routing = RouterModule.forChild(routes);