import { Routes, RouterModule } from '@angular/router';
import {AdminUsersComponent} from './adminUsers.component';

const routes: Routes = [
  {
    path: '',
    component: AdminUsersComponent,
  },
];

export const routing = RouterModule.forChild(routes);
