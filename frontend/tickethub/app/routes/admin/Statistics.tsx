import { useLoaderData, Link } from "react-router";
import StatisticsCharts from "~/components/admin/StatisticsUI";
import { adminService } from "~/services/AdminService";
import type { AdminStatisticsDTO } from "~/models/AdminStatistics";

export async function clientLoader() {
    const data = await adminService.getStatistics();
    return { data };
}

export default function StatisticsRoute() {
    const { data } = useLoaderData<{ data: AdminStatisticsDTO }>();

    return (
        <StatisticsCharts data={data} />
    );
}