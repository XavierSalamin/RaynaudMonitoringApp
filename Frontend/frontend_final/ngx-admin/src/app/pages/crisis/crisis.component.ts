import { Component } from '@angular/core';
import { LocalDataSource } from 'ng2-smart-table';

import { SmartTableService } from '../../@core/data/smart-table.service';
import { ActivatedRoute } from '@angular/router';
import { UserRequestService } from  '../../services/user-request.service';
@Component({
  selector: 'crisis',
   templateUrl: './crisis.component.html',

 styleUrls: ['./crisis.component.scss']
})
export class CrisisComponent {


  crisisArray: Array<any>;
  crisis: Array<any>;

  crisisMonth : any;
  private sub: any;
  userParams : any;
  data: Array<any>;
  imageToShow: any;
  isImageLoading : boolean;
  userRequest: any;

  userId : string;


  constructor(private userRequestService : UserRequestService, private route: ActivatedRoute ) {}

  ngOnInit() {


   this.sub = this.route.params.subscribe(params => {
       this.userParams = params; // (+) converts string 'id' to a number

     })
   //On passe l'id = concat(firstname+lastname) pour obtenir que les crises d'un user
   var str1 = String(this.userParams.firstname);
     var str2 = String(this.userParams.lastname);


   this.userId = str1.concat(str2);
   console.log(this.userId);
       this.userRequestService.getCrisisById(this.userId).subscribe(data => {

         //On récupère la liste de toutes les crisis
      this.crisisArray = data;

      //On récupère séparement chaque crisis de la liste pour les afficher dans une boucle for depuis le HTML
      for (let i in this.crisisArray) {
        this.crisis = this.crisisArray[i];
      }

      //Les mois sont enregistré dans de 0 à 11 dans la DB, on veut les afficher sur le front dans le bon format
        for (let i in this.crisisArray) {
        this.crisisMonth = this.crisisArray[i].month+1;
      }

    })
    //this.user["month"]=this.user["month"]+1;
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



    exportData(patientNumber){
             console.log("Patient Number : "+patientNumber);
    //Generate Excel
       this.userRequestService.getExcelById(patientNumber).subscribe(data => {
     
            console.log("Exporting Excel data for "+patientNumber);

    })
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