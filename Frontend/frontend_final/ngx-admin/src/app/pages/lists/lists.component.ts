import { Component } from '@angular/core';
import { LocalDataSource } from 'ng2-smart-table';

import { SmartTableService } from '../../@core/data/smart-table.service';

import { UserRequestService } from  '../../services/user-request.service';
@Component({
  selector: 'lists',
   templateUrl: './lists.component.html',

 styleUrls: ['./lists.component.scss']
})



export class ListsComponent {

  users: Array<any>;
  user: Array<any>;


  data: Array<any>;
  imageToShow: any;
isImageLoading : boolean;
  userRequest: any;


  constructor(private userRequestService : UserRequestService ) {}

  ngOnInit() {
    this.userRequestService.getAllProfile().subscribe(data => {
      this.users = data;
      for (let i in this.users) {
        this.user = this.users[i];
      }
    })
    this.getImageFromService();
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


  


getImageFromService() {
      this.isImageLoading = true;
      this.userRequestService.getPicture().subscribe(data => {
        this.createImageFromBlob(data);
        this.isImageLoading = false;
      }, error => {
        this.isImageLoading = false;
        console.log(error);
      });
}


createImageFromBlob(image: Blob) {
   let reader = new FileReader();
   reader.addEventListener("load", () => {
      this.imageToShow = reader.result;
   }, false);

   if (image) {
      reader.readAsDataURL(image);
   }
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