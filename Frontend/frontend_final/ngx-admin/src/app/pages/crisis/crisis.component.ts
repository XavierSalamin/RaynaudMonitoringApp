import {Component} from '@angular/core';
import {LocalDataSource} from 'ng2-smart-table';

import {SmartTableService} from '../../@core/data/smart-table.service';
import {ActivatedRoute} from '@angular/router';
import {UserRequestService} from '../../services/user-request.service';
import {FileHelper} from "../../../helper/file.helper";

@Component({
  selector: 'crisis',
  templateUrl: './crisis.component.html',

  styleUrls: ['./crisis.component.scss']
})
export class CrisisComponent {

  excelUrl: string;

  crisisArray: Array<any>;
  crisis: Array<any>;

  crisisMonth: any;
  private sub: any;
  userParams: any;
  data: Array<any>;
  crisisStartImage: Array<any>;
  crisisEndImage: Array<any>;
  isImageLoading: boolean;
  userRequest: any;

  userId: string;


  constructor(private userRequestService: UserRequestService, private route: ActivatedRoute) {
  }

  ngOnInit() {


    this.sub = this.route.params.subscribe(params => {
      this.userParams = params; // (+) converts string 'id' to a number

    })
    // On passe l'id = concat(firstname+lastname) pour obtenir que les crises d'un user
    var str1 = String(this.userParams.firstname);
    var str2 = String(this.userParams.lastname);


    this.userId = str2.concat(str1);
    console.log(this.userId);
    this.userRequestService.getCrisisById(this.userId).subscribe(data => {

      // On récupère la liste de toutes les crisis
      this.crisisArray = data;

      // On récupère séparement chaque crisis de la liste pour les afficher dans une boucle for depuis le HTML
      for (let i in this.crisisArray) {
        this.crisis = this.crisisArray[i];
      }


      this.crisisStartImage = [this.crisisArray.length];
      this.crisisEndImage = [this.crisisArray.length];
      // Les mois sont enregistré dans de 0 à 11 dans la DB, on veut les afficher sur le front dans le bon format
      for (let i in this.crisisArray) {
        this.crisisMonth = this.crisisArray[i].month + 1;
      }

      //On met les bonnes images
      for (let i in this.crisisArray) {
        this.getImageFromService(this.userId, this.crisisArray, i);
      }


    })
    // this.user["month"]=this.user["month"]+1;


  }


  skurt(user) {


    /*
          for (let i in datas) {
            this.data = datas[i];
            }

            */

    console.log(user.firstname);
    this.userRequestService.getUserRequest(user.firstname).subscribe(request => {
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


  exportData(patientNumber) {
    console.log("Patient Number : " + patientNumber);


    // Generate Excel
    this.userRequestService.getExcelById(patientNumber).subscribe(data => {


      FileHelper.downloadFile(new Blob([data],
        {type: 'application/vnd.ms-excel'}), "patient" + patientNumber + '_report');
    });


  }


  getImageFromService(id: string, crisis: any, i: any) {
    this.isImageLoading = true;
    if (crisis[i].startName && crisis[i].startName.length) {
      this.userRequestService.getPicture(id + '_pics', crisis[i].startName).subscribe(data => {
        this.createImageFromBlob(data, crisis, i, true);
        this.isImageLoading = false;
      }, error => {
        this.isImageLoading = false;
        console.log(error);
      });
    }
    if (crisis[i].endName && crisis[i].endName.length) {
      this.userRequestService.getPicture(id + '_pics', crisis[i].endName).subscribe(data => {
        this.createImageFromBlob(data, crisis, i, false);
        this.isImageLoading = false;
      }, error => {
        this.isImageLoading = false;
        console.log(error);
      });
    }

  }


  createImageFromBlob(image: Blob, crisis: any, i: number, isStart: boolean) {
    const reader = new FileReader();
    reader.addEventListener("load", () => {
      if (isStart === true) {
        this.crisisStartImage[i] = reader.result;
      }
      if (isStart === false) {
        this.crisisEndImage[i] = reader.result;
      }

    }, false);

    if (image) {
      reader.readAsDataURL(image);
    }
  }


}
