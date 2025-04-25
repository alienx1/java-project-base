"use client"

import { Card, CardHeader, CardBody, CardFooter } from "@heroui/react";

export interface LayoutCardConfig {
    className?: any;
    contentHeader?: React.ReactNode;
    contentBody: React.ReactNode;
    contentFooter?: React.ReactNode;
}

export default function LayoutCard(config: LayoutCardConfig) {
    return (
        <Card className={config.className} radius="md">
            {config.contentHeader && <CardHeader>{config.contentHeader}</CardHeader>}
            <CardBody>{config.contentBody}</CardBody>
            {config.contentFooter && <CardFooter>{config.contentFooter}</CardFooter>}
        </Card>
    );
}
