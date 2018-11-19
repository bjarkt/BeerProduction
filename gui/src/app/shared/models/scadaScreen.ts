export interface ScadaScreen {
    batchID: number | null;
    orderNumber: number | null;
    beerType: string | null;
    produced: number | null;
    accepted: number | null;
    defective: number | null;
    temperature: number | null;
    humidity: number | null;
    vibration: number | null;
    productAmount: number | null;
    machineSpeed: number | null;

    state: string;
}