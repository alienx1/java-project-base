"use client";

import LayoutCard from "@/components/layout/card";
import TitlePage from "@/components/title-page";
import { Button } from "@heroui/button";
import { Link } from "@heroui/link";



function ContentTable() {

    return (
        <>
            <LayoutCard
                contentBody={
                    <div className="flex gap-4">
                        <Button
                            showAnchorIcon
                            as={Link}
                            color="primary"
                            href="https://www.heroui.com/docs/components/table"
                            variant="solid"
                        >
                            docs table
                        </Button>
                    </div>
                }
            />
        </>
    );
}




export default function DemoTable() {
    return (
        <>
            <TitlePage name="Table" />
            <ContentTable />
        </>
    );
}