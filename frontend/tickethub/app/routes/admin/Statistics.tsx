import { useLoaderData, Link } from "react-router";
import StatisticsCharts from "~/components/admin/StatisticsUI";
import { getStatistics } from "~/services/adminService";
import type { AdminStatistics } from "~/models/AdminStatistics";

export async function clientLoader() {
    const data = await getStatistics();
    return { data };
}

export default function StatisticsRoute() {
    const { data } = useLoaderData<{ data: AdminStatistics }>();

    return (
        <StatisticsCharts data={data} />
    );
}