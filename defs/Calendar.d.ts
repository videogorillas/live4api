import TimeInterval from "./TimeInterval";
class Calendar implements Doc {
    intervals: { [id: string]: TimeInterval };
    _rev: number;
    id: string;

    getId (): string ;

    setId (id: string): void ;

    isActive (): boolean ;

    isBusyAt (interval: TimeInterval): boolean ;

}