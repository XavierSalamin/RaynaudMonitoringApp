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
  {
    title: 'Admin',
    icon: 'nb-locked',
    link: '/pages/adminUsers',
  },
  {
    title: 'Téléchargements',
    icon: 'nb-play',
    link: '/pages/release',
  },




];
