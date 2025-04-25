'use client'

import { BreadcrumbItem, Breadcrumbs } from "@heroui/react";

interface TitlePageProps {
    name: string;
}

export default function TitlePage({ name }: TitlePageProps) {
    return (
        <div className="flex flex-col flex-wrap gap-4 text-2xl font-semibold p-1">
            {name}
        </div>
    );
}
