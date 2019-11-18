import { Component } from '@angular/core';
import { LocalDataSource } from 'ng2-smart-table';

import { SmartTableService } from '../../../@core/data/smart-table.service';

import { UserRequestService } from  '../../../services/user-request.service';

@Component({
  selector: 'ngx-smart-table',
  templateUrl: './smart-table.component.html',
  styles: [`
    nb-card {
      transform: translate3d(0, 0, 0);
    }
  `],
})
export class SmartTableComponent {
  users: Array<any>;
  user: Array<any>;


  data: Array<any>;

  userRequest: any;
  constructor(private userRequestService : UserRequestService ) {}

  ngOnInit() {
    this.userRequestService.getAll().subscribe(data => {
      this.users = data;
      for (let i in this.users) {
        this.user = this.users[i];
      }
    })
    }

    skurt(user){


/*
      for (let i in datas) {
        this.data = datas[i];
        }

        */

         console.log(user.firstname);
      this.userRequestService.getUserRequest(user.firstname).subscribe(request=> {
        this.userRequest = request;
         console.log(this.userRequest.lastname);
      })
      /*
      this.userRequestService.getUserRequest(data.firstname).subscribe(data => {
        this.users = data;
        for (let i in this.users) {
        this.user = this.users[i];
        }
         console.log(this.user);
      })
     */
    }
//  source: LocalDataSource = new LocalDataSource();
/*
  constructor(private service: SmartTableService) {
    const data = this.service.getData();
    this.source.load(data);
  }
*/

/*
  onDeleteConfirm(event): void {
    if (window.confirm('Are you sure you want to delete?')) {
      event.confirm.resolve();
    } else {
      event.confirm.reject();
    }
  }
 */ 
}
