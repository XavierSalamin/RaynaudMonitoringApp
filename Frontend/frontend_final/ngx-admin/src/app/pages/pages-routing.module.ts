import {RouterModule, Routes} from '@angular/router';
import {NgModule} from '@angular/core';

import {PagesComponent} from './pages.component';
import {NotFoundComponent} from './miscellaneous/not-found/not-found.component';

const routes: Routes = [{
  path: '',
  component: PagesComponent,
  children: [
    {
      path: 'bootstrap',
      loadChildren: './bootstrap/bootstrap.module#BootstrapModule',
    }, {
      path: 'tables',
      loadChildren: './tables/tables.module#TablesModule',
    }, {
      path: 'miscellaneous',
      loadChildren: './miscellaneous/miscellaneous.module#MiscellaneousModule',
    },
    {
      path: 'new',
      loadChildren: './new/new.module#NewModule',
    },
    {
      path: 'release',
      loadChildren: './release/release.module#ReleaseModule',
    },
    {
      path: 'lists',
      loadChildren: './lists/lists.module#ListsModule',
    },
    {
      path: 'crisis',
      loadChildren: './crisis/crisis.module#CrisisModule',
    },
    {
      path: 'adminUsers',
      loadChildren: './adminUsers/adminUsers.module#AdminUsersModule',
    },
    {
      path: 'auth ',
      loadChildren: './auth/auth.module#NgxAuthModule',
    },


    {
      path: '',
      redirectTo: 'lists',
      pathMatch: 'full',
    }, {
      path: '**',
      component: NotFoundComponent,
    }],
}];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule],
})
export class PagesRoutingModule {
}
