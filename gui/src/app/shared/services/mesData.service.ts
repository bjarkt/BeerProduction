import { Injectable } from "@angular/core";
import {
  HttpClient,
  HttpHeaders,
  HttpErrorResponse,
  HttpResponse
} from "@angular/common/http";
import { Observable, of } from "rxjs";
import { map, catchError, tap } from "rxjs/operators";
import { Order } from "../models/order";
import { environment } from "../../../environments/environment";
import { ProductionInfo } from "../models/ProductionInfo";

const MesEndpoint = environment.mesUrl;


const httpOptions = {
  headers: new HttpHeaders({
    "Content-Type": "application/json"
  })
};

@Injectable()
export class MesDataService {

    public loading: boolean;

    constructor(private http: HttpClient) { }

    private extractData(res: Response) {
        let body = res;
        return body || {};
    }

  
    public getMesOrders(): Observable<any> {
        return this.http.get(MesEndpoint + 'view-orders', httpOptions);
    }

    public getMesOrderItems(orderNumber: number): Observable<any> {
        return this.http.get(MesEndpoint + 'view-order-items/' + orderNumber, httpOptions);
    }

    public viewStatistics(days?: number): Observable<any> {
        if (days) {
            return this.http.get(MesEndpoint + 'get-plant-statistics/?days=' + days, httpOptions);
        } else {
            return this.http.get(MesEndpoint + 'get-plant-statistics/', httpOptions)
        }
    }

    public getBatches(): Observable<any> {
        return this.http.get(MesEndpoint + 'view-all-batches', httpOptions)
    }

    public createBatches(batches: ProductionInfo[]){
        const batchesMap = {orderItems: batches} 
        return this.http.post(MesEndpoint + 'create-batches', batchesMap, httpOptions)
    }

    public getReport(batchId: number): Observable<any> {
        return this.http.get(MesEndpoint + 'get-report/' + batchId, {...httpOptions, responseType: 'blob'})
    }

    public downloadFile(data: any, type: string) {
        const blob = new Blob([data], {type: type.toString()})
        const url = window.URL.createObjectURL(blob)
        window.open(url)
    }

    public getOEE(batchId: number): Observable<any> {
        return this.http.get(MesEndpoint + 'get-oee/' + batchId, httpOptions)
    }

    public getSavingMachSpeed(beerType: string): Observable<any>{
        return this.http.get(MesEndpoint + 'get-saving-machspeed/' + beerType, httpOptions);
    }

    public getProfitableMachSpeed(beerType: string): Observable<any>{
        return this.http.get(MesEndpoint + 'get-profitable-machspeed/' + beerType, httpOptions);
    }

    public getFastestMachSpeed(beerType: string, quantity: number): Observable<any>{
        return this.http.get(MesEndpoint + 'get-fastest-machspeed/' + beerType + '/' + quantity, httpOptions);
    }
}
