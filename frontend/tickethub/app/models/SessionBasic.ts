
export interface SessionBasic {
    sessionID: number,
    date: string        //To convert to date nacessary to do new Date(session.date).toLocaleString("es-ES");
}