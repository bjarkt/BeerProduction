import { Injectable } from "@angular/core";
import { HttpClient } from '@angular/common/http';
import { Observable } from "rxjs";

@Injectable()
export class DataService {
    ErpUrl = 'http://localhost:7002';
    MesUrl = 'http://localhost:7001';
    ScadaUrl = 'http://localhost:7000';


    constructor(private http: HttpClient){}

    //Template
    public uploadImage(title: string, image: File, albumId?: number): Observable<void>{
        const uploadData = new FormData();
        uploadData.append('myFile',image, image.name);
        console.log(uploadData);
        return null;
    }
  


}