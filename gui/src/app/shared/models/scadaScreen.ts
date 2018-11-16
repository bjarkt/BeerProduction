export interface ScadaScreen {
    batchID: number;
    orderNumber: number;
    beerType: string;
    produced: number;
    accepted: number;
    defective: number;
    temperature: number;
    humidity: number;
    vibration: number;
    productAmount: number;
    machineSpeed: number;

    state: string;
}