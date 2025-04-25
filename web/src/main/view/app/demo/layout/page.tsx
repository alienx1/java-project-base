'use client'
import LayoutCard, { LayoutCardConfig } from "@/components/layout/card";
import LayoutTab, { LayoutTabConfig } from "@/components/layout/tab"
import { ColorVariant, StyleVariant } from "@/components/layout/variant"
import TitlePage from "@/components/title-page";


function ContentLayoutTab() {
    const config: LayoutTabConfig = {
        className: "w-full",
        color: ColorVariant.Primary,
        disabled: [],
        style: StyleVariant.Light,
        tabs: [
            {
                key: "First",
                title: <h1>First</h1>,
                body: (
                    <div className="p-4">
                        <h2 className="text-xl font-semibold">First</h2>
                        <p>This is the First content.</p>
                    </div>
                ),
            },
            {
                key: "Second",
                title: <h1>Second</h1>,
                body: (
                    <div className="p-4">
                        <h2 className="text-xl font-semibold">Second</h2>
                        <p>This is the Second content.</p>
                    </div>
                ),
            },
        ],
    };
    return (
        <LayoutTab {...config} />
        /* Another way to write */
        //     <LayoutTab
        //     sizeClassName="w-full"
        //     color={ColorVariant.Primary}
        //     disabled={["settings"]}
        //     style={StyleVariant.Light}
        //     tabs={[{
        //         key: "First",
        //         title: (<h1>First</h1>),
        //         body: (
        //             <div className="p-4">
        //                 <h2 className="text-xl font-semibold">First</h2>
        //                 <p>This is the First content.</p>
        //             </div>
        //         ),
        //     }, {
        //         key: "Second",
        //         title: (<h1>Second</h1>),
        //         body: (
        //             <div className="p-4">
        //                 <h2 className="text-xl font-semibold">Second</h2>
        //                 <p>This is the Second content.</p>
        //             </div>
        //         ),
        //     }]}
        // />
    )
}

function ContentLayoutCard() {
    function contentHeader() {
        return (
            <h1>Header layer</h1>
        )
    }
    function contentBody() {
        return (
            <h1>Body layer</h1>
        )
    }
    function contentFooter() {
        return (
            <h1>Foote layer</h1>
        )
    }
    const config: LayoutCardConfig = {
        className: "w-full",
        contentHeader: contentHeader(),
        contentBody: contentBody(),
        contentFooter: contentFooter()
    }

    return (
        <LayoutCard {...config} />
    )
}

export default function DemoLayout() {
    return (
        <>
        <TitlePage name="Layout"/>
        <ContentLayoutTab />
        <ContentLayoutCard />
        </>
    );
}

