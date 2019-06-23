import { NbMenuItem } from '@nebular/theme';

export const MENU_ITEMS: NbMenuItem[] = [

  {
    title: 'Ajouter un patient',
    icon: 'nb-person',
    link: '/pages/new',
  },
  {
    title: 'Requêtes d\'inscriptions',
    icon: 'nb-plus',
    link: '/pages/tables/smart-table',
  },

   {
    title: 'Listes',
    icon: 'nb-tables',
    link: '/pages/lists',
  },

/*
  {
    title: 'Miscellaneous',
    icon: 'nb-shuffle',
    children: [
      {
        title: '404',
        link: '/pages/miscellaneous/404',
      },
    ],
  },
  {
    title: 'Auth',
    icon: 'nb-locked',
    children: [
      {
        title: 'Login',
        link: '/auth/login',
      },
      {
        title: 'Register',
        link: '/auth/register',
      },
      {
        title: 'Request Password',
        link: '/auth/request-password',
      },
      {
        title: 'Reset Password',
        link: '/auth/reset-password',
      },
    ],
  },
  */
];
