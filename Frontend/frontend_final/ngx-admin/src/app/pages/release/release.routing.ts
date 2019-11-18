
import { Routes, RouterModule } from '@angular/router';

import {ReleaseComponent} from './release.component';

const routes: Routes = [
  {
    path: '',
    component: ReleaseComponent,
  },
];

export const routing = RouterModule.forChild(routes);

