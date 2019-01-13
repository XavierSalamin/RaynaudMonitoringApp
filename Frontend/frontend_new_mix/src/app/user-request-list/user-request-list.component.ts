import { Component, OnInit } from '@angular/core';
import { UserRequestService } from '../shared/user-request/user-request.service';

@Component({
  selector: 'app-user-request-list',
  templateUrl: './user-request-list.component.html',
  styleUrls: ['./user-request-list.component.scss']
})
export class UserRequestListComponent implements OnInit {
  users: Array<any>;
  user: Array<any>;
  imageToShow: any;
isImageLoading : boolean;
  constructor(private userRequestService : UserRequestService) { }

  ngOnInit() {
  	this.userRequestService.getAll().subscribe(data => {
    	this.users = data;

      for (let i in this.users) {
        this.user = this.users[i];
      }

    })

this.getImageFromService();
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

}
